package com.mycx26.base.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 流程实例
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_inst")
public class ProcInst implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 流程实例名称
     */
    private String procInstName;

    /**
     * 流程定义key
     */
    private String procDefKey;

    /**
     * 流水号
     */
    private String flowNo;

    /**
     * 实例状态code
     */
    private String statusCode;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人邮箱
     */
    private String creatorEmail;

    /**
     * 创建人部门code
     */
    private String creatorDeptCode;

    /**
     * 创建人部门
     */
    private String creatorDept;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 完成时间
     */
    private LocalDateTime endTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Boolean yn;

    // enums
    @TableField(exist = false)
    private String procInstStatus;
}
