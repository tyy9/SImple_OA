package com.myoa.my_oa.service;

import com.myoa.my_oa.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myoa.my_oa.entity.dto.SubjectDto;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-18
 */
public interface SubjectService extends IService<Subject> {
        List<SubjectDto> getAllSubjectDto();//获取所有分类
        List<SubjectDto> getSubjectDtoById(int id);//根据id获取分类
}

