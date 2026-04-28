package com.example.filestorage.DTO;

import com.example.filestorage.Enum.ResponseStatus;
public class ResponseDTO {
    private ResponseStatus status;
    private String message;
    private Object detail;
    private Object details;

    public ResponseDTO(ResponseStatus status, String message, Object detail, Object details) {
        this.status = status;
        this.message = message;
        this.detail = detail;
        this.details = details;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    public static ResponseDTO success(String message) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, null,null);
    }

    public static ResponseDTO success(String message, Object detail) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, detail,null);
    }

    public static ResponseDTO success(Object details, String message) {
        return new ResponseDTO(ResponseStatus.SUCCESS, message, null, details);
    }

    public static ResponseDTO error(String message) {
        return new ResponseDTO(ResponseStatus.ERROR, message, null,null);
    }

    public static ResponseDTO error(String message, Object detail) {
        return new ResponseDTO(ResponseStatus.ERROR, message, detail,null);
    }

    public static ResponseDTO error(Object details, String message) {
        return new ResponseDTO(ResponseStatus.ERROR, message, null, details);
    }

    public static ResponseDTO notFound(String message) {
        return new ResponseDTO(ResponseStatus.NOT_FOUND, message, null,null);
    }

    public static ResponseDTO badRequest(String message) {
        return new ResponseDTO(ResponseStatus.BAD_REQUEST, message, null,null);
    }

    public static ResponseDTO unauthorized(String message) {
        return new ResponseDTO(ResponseStatus.UNAUTHORIZED, message, null, null);
    }

    public static ResponseDTO internalError(String message) {
        return new ResponseDTO(ResponseStatus.INTERNAL_SERVER_ERROR, message, null,null);
    }
}
