package com.solver.solver_be.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /*============================ SUCCESS ================================*/

    // User Success
    SIGN_UP_SUCCESS(200, "회원 가입이 완료되었습니다."),
    LOG_IN_SUCCESS(200, "로그인이 완료되었습니다."),
    FIND_USER_ID(200, "아이디 찾기 성공"),
    PASSWORD_RESET_SUCCESS(200, "비밀번호 재설정 성공"),
    LOBBYID_SIGN_UP(200, "로비 아이디 생성 완료" ),

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
    ACCESS_OUT_SUCCESS(200, "안녕히 가십시오" ),
    ACCESS_STATUS_SUCCESS(200, "출입관리 목록 불러오기 완료."),

    // Email Success
    EMAIL_CHECK(200, "이메일 전송 완료."),

    // Excel Success
    EXCEL_DOWNLOAD_SUCCESS(200, "엑셀 다운로드 완료" ),

    // Notification Success
    NOTIFICATION_DELETE_SUCCESS(200, "알람 삭제 완료"),
    NOTIFICATIONS_DELETE_SUCCESS(200, "알람 전체 삭제 완료"),

    /*============================ FAIL ================================*/

    //Global Fail
    NOT_VALID_REQUEST(400, "유효하지 않은 요청입니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(400, "토큰을 찾을 수 없습니다."),
    NOT_VALID_REFRESH_TOKEN(400, "유효하지 않은 리프레시 토큰입니다."),

    // User Fail
    USER_ID_EXIST(400, "이미 존재하는 아이디입니다."),
    USER_NICKNAME_EXIST(400, "이미 존재하는 닉네임입니다."),
    USER_ACCOUNT_NOT_EXIST(400, "존재하지 않는 계정입니다."),
    USER_NOT_FOUND(400, "존재하지 않는 사용자입니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    INVALID_COMPANY_TOKEN(400, "유효하지 않은 회사 코드입니다."),
    AUTH_FAILED(400, "이메일 인증 실패" ),
    LOOBBYID_ALREADY_DONE(400, "로비 아이디가 발급 되어있습니다." ),

    // Visitor Fail
    VISITFORM_NOT_FOUND(400, "방문 신청 기록이 없습니다."),
    VISITFORM_UPDATE_FAILED(400, "본인은 방문 기록이 아닙니다."),
    VISITFORM_EXIST(400, "동일한 시간에 방문 기록이 있습니다."),
    ADMIN_NOT_FOUND(400, "담당자가 없습니다."),

   // Company Fail
    COMPANY_ALREADY_EXIST(400, "등록된 회사가 이미 존재합니다."),
    COMPANY_NOT_FOUND(400, "회사를 찾을 수 없습니다."),
    ACCESS_RECORD_NOT_FOUND(400, "출입기록을 찾을 수 없습니다." ),
    ACCESS_IN_ALREADY_DONE(401, "이미 체크인 되었습니다." ),
    ACCESS_OUT_ALREADY_DONE(402, "이미 체크아웃 되었습니다." );

    private final int statusCode;
    private final String message;
}