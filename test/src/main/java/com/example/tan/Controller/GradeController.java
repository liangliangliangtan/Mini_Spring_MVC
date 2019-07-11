package com.example.tan.Controller;

import com.example.tan.Service.GradeService;
import com.example.tan.beans.Autowired;
import com.example.tan.web.mvc.Controller;
import com.example.tan.web.mvc.RequestMapping;
import com.example.tan.web.mvc.RequestParam;

@Controller
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @RequestMapping("/get_grade.do")
    public Integer getGrade(@RequestParam(value = "name") String name, @RequestParam(value = "uid") String uid){
        return gradeService.calGrade(Integer.parseInt(uid));
    }
}
