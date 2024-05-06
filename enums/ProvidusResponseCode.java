package ng.optisoft.rosabon.enums;

import lombok.Getter;

@Getter
public enum ProvidusResponseCode {
            SUCCESS ("00"),
            DUPLICATE_TRANSACTION("01"),
            REJECTED_TRANSACTION("02"),
           SYSTEM_FAILURE_RETRY("03");

            private String code;
    ProvidusResponseCode(String s) {
        code = s;
    }
}
