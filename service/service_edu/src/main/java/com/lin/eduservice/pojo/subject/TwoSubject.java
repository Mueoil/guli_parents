package com.lin.eduservice.pojo.subject;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TwoSubject {
    private String id;
    private String title;
}
