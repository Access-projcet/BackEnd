package com.solver.solver_be.global.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

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
    LOBBYID_ALREADY_DONE(400, "로비 아이디가 발급 되어있습니다."),
    NOT_IMPOSSIBLE_TRY(400, "일정 횟수를 실패하여 일정 시간 시도가 불가능 합니다." ),

    // Visitor Fail
    VISITFORM_NOT_FOUND(400, "방문 신청 기록이 없습니다."),
    VISITFORM_UPDATE_FAILED(400, "본인은 방문 기록이 아닙니다."),
    VISITFORM_EXIST(400, "동일한 시간에 방문 기록이 있습니다."),
    ADMIN_NOT_FOUND(400, "담당자가 없습니다."),
    NOT_VALID_STATUS(400, "2 = 승인,3 = 거절 하나로 보내주십시오."),

    // Company Fail
    COMPANY_ALREADY_EXIST(400, "등록된 회사가 이미 존재합니다."),
    COMPANY_NOT_FOUND(400, "회사를 찾을 수 없습니다."),
    COMPANY_NOT_EQUALS(400, "관리자의 회사와 동일하지 않습니다."),

    // Access Fail
    ACCESS_RECORD_NOT_FOUND(400, "출입기록을 찾을 수 없습니다." ),
    ACCESS_IN_ALREADY_DONE(401, "이미 체크인 되었습니다." ),
    ACCESS_OUT_ALREADY_DONE(402, "이미 체크아웃 되었습니다." ),

    // Notification Fail
    NOTIFICATION_NOT_FOUND(400, "알람을 찾을 수 없습니다."),
    CONTENT_NOT_VALID(400, "컨텐츠가 올바르지 않습니다." );

    private final int statusCode;
    private final String message;

}