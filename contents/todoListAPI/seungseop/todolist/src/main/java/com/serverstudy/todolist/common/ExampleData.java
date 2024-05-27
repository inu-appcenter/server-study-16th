package com.serverstudy.todolist.common;

public interface ExampleData {

    String INVALID_PARAMETER_DATA = """
            {
              "timestamp": "2024-05-15T12:32:00.1832345",
              "status": 400,
              "error": "BAD_REQUEST",
              "code": "INVALID_PARAMETER",
              "message": [
                  "[userId] 널이어서는 안됩니다 입력된 값: [null]"
              ]
            }
            """;
    String USER_NOT_FOUND_DATA = """
            {
              "timestamp": "2024-05-15T11:30:54.8218419",
              "status": 404,
              "error": "NOT_FOUND",
              "code": "USER_NOT_FOUND",
              "message": [
                "해당 유저 정보를 찾을 수 없습니다"
              ]
            }
            """;
    String TODO_NOT_FOUND_DATA = """
            {
              "timestamp": "2024-05-15T11:30:54.8218419",
              "status": 404,
              "error": "NOT_FOUND",
              "code": "TODO_NOT_FOUND",
              "message": [
                "해당 투두 정보를 찾을 수 없습니다"
              ]
            }
            """;
    String FOLDER_NOT_FOUND_DATA = """
            {
              "timestamp": "2024-05-15T11:30:54.8218419",
              "status": 404,
              "error": "NOT_FOUND",
              "code": "FOLDER_NOT_FOUND",
              "message": [
                "해당 폴더 정보를 찾을 수 없습니다"
              ]
            }
            """;
    String DUPLICATE_USER_EMAIL_DATA = """
            {
              "timestamp": "2024-05-15T12:37:24.9740069",
              "status": 409,
              "error": "CONFLICT",
              "code": "DUPLICATE_USER_EMAIL",
              "message": [
                "해당 이메일이 이미 존재합니다"
              ]
            }
            """;
    String DUPLICATE_FOLDER_NAME_DATA = """
            {
              "timestamp": "2024-05-15T12:38:10.4979864",
              "status": 409,
              "error": "CONFLICT",
              "code": "DUPLICATE_FOLDER_NAME",
              "message": [
                "해당 폴더명이 이미 존재합니다."
              ]
            }
            """;

}
