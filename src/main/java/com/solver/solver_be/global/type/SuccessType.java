package com.solver.solver_be.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessType {

    /*============================ SUCCESS ================================*/

    // User Success
    SIGN_UP_SUCCESS(200, "회원 가입이 완료되었습니다."),
    LOG_IN_SUCCESS(200, "로그인이 완료되었습니다."),
    FIND_USER_ID(200, "아이디 찾기 성공"),
    PASSWORD_RESET_SUCCESS(200, "비밀번호 재설정 성공"),
    LOBBYID_SIGN_UP(200, "로비 아이디 생성 완료, 이메일을 확인해주세요."),

    // VisitForm Success
    VISITFORM_WRITE_SUCCESS(200, "방문 문서 작성 성공"),
    VISITFORM_GET_SUCCESS(200, "방문 신청 내용 조회 성공"),
    VISITFORM_UPDATE_SUCCESS(200, "방문 기록 수정 성공"),
    VISITFORM_DELETE_SUCCESS(200,"방문 기록 삭제 성공"),
    VISITFORM_STATUS_UPDATE_SUCCESS(200, "방문자 상태 변경 성공"),
    VISITFORM_SEARCH_SUCCESS(200, "정렬 완료."),

    // Token Success
    TOKEN_UPDATE_SUCCESS(200, "토큰이 업데이트되었습니다."),

    // Company Success
    COMPANY_REGISTER_SUCCESS(200, "회사 정보 등록을 완료했습니다."),
    COMPANY_GET_SUCCESS(200, "등록된 회사 가져오기 성공했습니다. "),
    COMPANY_UPDATE_SUCCESS(200, "회사 정보 변경을 완료했습니다."),
    COMPANY_DELETE_SUCCESS(200, "회사 정보 삭제를 완료했습니다."),
    ACCESS_IN_SUCCESS(200, "어서오십시오" ),
    ACCESS_OUT_SUCCESS(200, "안녕히 가십시오"),
    ACCESS_STATUS_SUCCESS(200, "출입관리 목록 불러오기 완료."),

    // Email Success
    EMAIL_CHECK(200, "이메일 전송 완료."),

    // Excel Success
    EXCEL_DOWNLOAD_SUCCESS(200, "엑셀 다운로드 완료" ),

    // Notification Success
    NOTIFICATION_DELETE_SUCCESS(200, "알람 삭제 완료"),
    NOTIFICATIONS_DELETE_SUCCESS(200, "알람 전체 삭제 완료"),
    NOTIFICATIONS_READ_SUCCESS(200, "알람 확인 완료");

    private final int statusCode;
    private final String message;

}