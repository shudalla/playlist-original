package br.com.shu.playlist.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 5120698653021961500L;

    private final String msg;

    public UnauthorizedException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}