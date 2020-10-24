package com.home.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liqingdong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;

    private String name;

    private int age;

    private List<String> tags;
}
