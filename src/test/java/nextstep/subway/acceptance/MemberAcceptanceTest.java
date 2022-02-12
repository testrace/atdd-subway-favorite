package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberFixture.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> response = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(response, EMAIL, AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, "new" + EMAIL, "new" + PASSWORD, AGE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_응답);

        ExtractableResponse<Response> 회원_정보_조회_응답 = 회원_정보_조회_요청(회원_생성_응답);
        회원_정보_조회됨(회원_정보_조회_응답, EMAIL, AGE);

        ExtractableResponse<Response> 회원_정보_수정_응답 = 회원_정보_수정_요청(회원_생성_응답, "e@mail.com", PASSWORD, AGE);
        회원_정보_수정됨(회원_정보_수정_응답);

        ExtractableResponse<Response> 회원_삭제_응답 = 회원_삭제_요청(회원_생성_응답);
        회원_정보_삭제됨(회원_삭제_응답);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        ExtractableResponse<Response> 회원_생성_응답 = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_응답);

        String token = 로그인_되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> 내_회원_정보_조회_응답 = 내_회원_정보_조회_요청(token);
        회원_정보_조회됨(내_회원_정보_조회_응답, EMAIL, AGE);

        ExtractableResponse<Response> 내_회원_정보_수정_응답 = 내_회원_정보_수정_요청(token, "e@mail.com", PASSWORD, AGE);
        회원_정보_수정됨(내_회원_정보_수정_응답);

        ExtractableResponse<Response> 내_회원_삭제_응답 = 내_회원_삭제_요청(token);
        회원_정보_삭제됨(내_회원_삭제_응답);

    }

    @DisplayName("비로그인 상태에서 내 정보 관리 예외")
    @Test
    void manageMyInfoUnauthorizedException() {
        //given
        String 유효하지_않은_토큰 = "invalid token";

        ExtractableResponse<Response> 내_회원_정보_조회_응답 = 내_회원_정보_조회_요청(유효하지_않은_토큰);
        권한_없음(내_회원_정보_조회_응답);

        ExtractableResponse<Response> 내_회원_정보_수정_응답 = 내_회원_정보_수정_요청(유효하지_않은_토큰, "e@mail.com", PASSWORD, AGE);
        권한_없음(내_회원_정보_수정_응답);

        ExtractableResponse<Response> 내_회원_삭제_응답 = 내_회원_삭제_요청(유효하지_않은_토큰);
        권한_없음(내_회원_삭제_응답);

    }
}
