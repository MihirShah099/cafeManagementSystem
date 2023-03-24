package com.inn.cafe.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class userWrapper {
    Integer id;
    private String name;

    public userWrapper(Integer id, String name, String contactNumber, String email, String status) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;

        this.status = status;

    }

    private String contactNumber;
    private String email;

    private String status;


}
