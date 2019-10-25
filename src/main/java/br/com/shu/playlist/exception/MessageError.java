package br.com.shu.playlist.exception;

import java.io.Serializable;

import br.com.shu.playlist.component.Dictionary;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MessageError implements Serializable {

    private static final long serialVersionUID = 5256651766510189965L;

    private String code;
    private String message;

    public MessageError(String code, Object... args) {
        this.code = code;
        this.message = Dictionary.valueOf(code, args);
    }

}
