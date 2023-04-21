package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.myoa.my_oa.entity.Subject;
import com.myoa.my_oa.entity.dto.SubjectDto;
import com.myoa.my_oa.mapper.SubjectMapper;
import com.myoa.my_oa.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-18
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<SubjectDto> getAllSubjectDto() {
        //首先找出所有父类别
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(Subject::getParentId,0);
        List<Subject> father_list = this.list(subjectLambdaQueryWrapper);
        //找出所有子类别
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper1.ne(Subject::getParentId,0);
        ArrayList<SubjectDto> subjectDtos = new ArrayList<>();
        List<Subject> children_list = this.list(subjectLambdaQueryWrapper1);
        //根据id与parentid的对比获取集合
        for(Subject s:father_list){
            SubjectDto subjectDto = new SubjectDto();
            subjectDto.setId(s.getId());
            subjectDto.setTitle(s.getTitle());
            subjectDto.setParentId(s.getParentId());
            ArrayList<Subject> c_list = new ArrayList<>();
            for(Subject s2:children_list){
                if(s2.getParentId().equals(s.getId())){
                    c_list.add(s2);
                }
            }
            subjectDto.setChildren(c_list);
            subjectDtos.add(subjectDto);
        }
            return subjectDtos;
    }

    @Override
    public List<SubjectDto> getSubjectDtoById(int id) {
        //首先找出所有父类别
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper.eq(Subject::getParentId,0)
                .eq(Subject::getId,id);
        List<Subject> father_list = this.list(subjectLambdaQueryWrapper);
        //找出所有子类别
        LambdaQueryWrapper<Subject> subjectLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        subjectLambdaQueryWrapper1.ne(Subject::getParentId,0);
        ArrayList<SubjectDto> subjectDtos = new ArrayList<>();
        List<Subject> children_list = this.list(subjectLambdaQueryWrapper1);
        //根据id与parentid的对比获取集合
        for(Subject s:father_list){
            SubjectDto subjectDto = new SubjectDto();
            subjectDto.setId(s.getId());
            subjectDto.setTitle(s.getTitle());
            subjectDto.setParentId(s.getParentId());
            ArrayList<Subject> c_list = new ArrayList<>();
            for(Subject s2:children_list){
                if(s2.getParentId().equals(s.getId())){
                    c_list.add(s2);
                }
            }
             subjectDto.setChildren(c_list);
            subjectDtos.add(subjectDto);
        }
        return subjectDtos;
    }
}
