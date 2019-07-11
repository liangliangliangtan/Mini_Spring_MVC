package com.example.tan.Service;


import com.example.tan.beans.Bean;

@Bean
public class GradeService {

    public Integer calGrade(Integer uid){
        if(uid == 1) return 100;
        else if(uid == 2) return 90;
        else return 70;
    }
}
