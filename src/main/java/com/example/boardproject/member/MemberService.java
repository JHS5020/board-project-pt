package com.example.boardproject.member;

import com.example.boardproject.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Service: 이 클래스가 "비즈니스 로직을 담당하는 계층"임을 스프링에 알리는 어노테이션.
 *           내부적으로는 @Component와 거의 동일하게 동작하지만(스프링 빈으로 등록),
 *           의미상 서비스 계층임을 명확히 드러내는 용도로 씁니다.
 *
 * @RequiredArgsConstructor: Lombok이 final 필드들을 파라미터로 받는 생성자를 자동 생성.
 *           스프링은 생성자가 하나뿐이면 @Autowired 없이도 그 생성자로 자동 주입해줍니다.
 *           (필드에 직접 @Autowired 붙이는 방식보다 생성자 주입이 실무 표준입니다 -
 *            테스트하기 쉽고, 필드가 final이라 불변성이 보장되고, 순환 참조를 컴파일 시점에 잡을 수 있어서)
 *
 * @Transactional: 이 클래스의 모든 public 메서드를 하나의 트랜잭션으로 묶습니다.
 *           메서드 도중 예외가 발생하면 그동안의 DB 변경사항이 전부 롤백됩니다.
 *           (readOnly = true를 클래스 레벨 기본값으로 주고, 쓰기 작업이 필요한 메서드에만
 *            개별적으로 @Transactional을 다시 붙여서 readOnly를 해제하는 게 실무 패턴입니다 - 아래 참고)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입.
     *
     * @Transactional (readOnly = false, 즉 쓰기 트랜잭션)을 메서드에 다시 붙인 이유:
     *   클래스 레벨의 readOnly = true는 "읽기 전용 최적화"가 걸려서 변경 감지(dirty checking) 등이
     *   생략되기 때문에 조회 성능에 유리합니다. 하지만 저장(save)처럼 실제로 DB에 쓰기가 필요한
     *   메서드는 readOnly를 꺼줘야 정상 동작합니다. 그래서 "클래스는 기본 읽기 전용,
     *   쓰기가 필요한 메서드만 개별적으로 오버라이드"하는 패턴을 씁니다.
     */
    @Transactional
    public Long signUp(String username, String rawPassword, String nickname, String email) {
        validateDuplicateUsername(username);
        validateDuplicateEmail(email);

        String encodedPassword = passwordEncoder.encode(rawPassword);

        Member member = Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .email(email)
                .role(Role.USER) // 회원가입은 항상 일반 회원으로 시작 (관리자는 별도 방식으로 부여)
                .build();

        Member saved = memberRepository.save(member);
        return saved.getId();
    }

    private void validateDuplicateUsername(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new BusinessException("이미 사용 중인 아이디입니다.");
        }
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BusinessException("이미 사용 중인 이메일입니다.");
        }
    }
}