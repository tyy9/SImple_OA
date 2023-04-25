package com.myoa.my_oa.service.impl;

import com.myoa.my_oa.entity.Comment;
import com.myoa.my_oa.mapper.CommentMapper;
import com.myoa.my_oa.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
