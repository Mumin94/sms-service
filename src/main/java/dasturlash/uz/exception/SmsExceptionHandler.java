package dasturlash.uz.exception;

import dasturlash.uz.dto.response.SmsSendResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class SmsExceptionHandler {

    @ExceptionHandler({RestClientException.class, IllegalStateException.class})
    public ResponseEntity<SmsSendResponseDTO> handleSendingFailure(Exception ex) {

        SmsSendResponseDTO response = new SmsSendResponseDTO();
        response.setSuccess(false);
        response.setMock(false);
        response.setMessage("SMS provider request failed. Check credentials, balance and approved message text.");

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }
}