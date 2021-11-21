package com.cheng.manage.service.impl;

import com.cheng.manage.model.Notice;
import com.cheng.manage.mapper.NoticeMapper;
import com.cheng.manage.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知公告表 服务实现类
 * </p>
 *
 * @author weicheng
 * @since 2021-11-21
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

}
