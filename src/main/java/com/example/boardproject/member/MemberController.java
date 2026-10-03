package com.example.boardproject.member;

import com.example.boardproject.member.dto.MemberSignUpRequest;
import com.example.boardproject.member.dto.MemberSignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @RestController: @Controller + @ResponseBody를 합친 것.
 *      메서드 리턴값이 뷰 이름이 아니라 그대로 응답 바디(JSON 등)로 직렬화됩니다.
 *      REST API 서버에서는 거의 항상 이걸 씁니다.
 *
 * @RequestMapping("/api/members"): 이 컨트롤러의 모든 엔드포인트 앞에 공통으로 붙는 경로.
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication) {
        // 인증 안 된 상태로 여기까지 오면 Security가 먼저 401을 던지고 컨트롤러까지 못 옴
        return ResponseEntity.ok(authentication.getName()); // 로그인된 username 반환
    }
    /**
     * @Valid: 파라미터로 받은 MemberSignUpRequest에 붙어있는 검증 어노테이션(@NotBlank 등)을
     *         Spring이 요청을 처리하기 직전에 자동으로 검사합니다.
     *         검증 실패 시 MethodArgumentNotValidException이 발생하고,
     *         (아직 우리가 예외 처리기를 안 만들어서 지금은 Spring 기본 400 에러 응답이 나갑니다 -
     *          나중에 @ExceptionHandler로 응답 형식을 예쁘게 다듬을 예정)
     *
     * ResponseEntity: 응답의 HTTP 상태 코드와 바디를 함께 제어할 수 있게 해주는 래퍼 클래스.
     *      회원가입 성공은 관례적으로 201 Created를 씁니다 (새 리소스가 생성됐다는 의미).
     */
    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponse> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        Long memberId = memberService.signUp(
                request.getUsername(),
                request.getPassword(),
                request.getNickname(),
                request.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MemberSignUpResponse(memberId));
    }
}