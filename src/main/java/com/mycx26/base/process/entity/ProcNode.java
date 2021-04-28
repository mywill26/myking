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
 * 流程节点
 * </p>
 *
 * @author mycx26
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_node")
public class ProcNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 节点key
     */
    private String nodeKey;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 流程定义key
     */
    private String procDefKey;

    /**
     * 节点类别code
     */
    private String categoryCode;

    /**
     * 子表
     */
    private String nodeHandler;

    /**
     * 分派人变量
     */
    private String assigneesVar;    // ignore

    /**
     * 分派人类型code
     */
    private String assigneeTypeCode;    // ignore

    /**
     * 角色code
     */
    private String roleCode;    // ignore

    /**
     * 渲染视图key
     */
    private String viewKey;

    /**
     * 审批按钮名称
     */
    private String approveName;

    /**
     * 是否审批
     */
    @TableField("is_approve")
    private Boolean approve;

    /**
     * 是否驳回上级
     */
    @TableField("is_reject_previous")
    private Boolean rejectPrevious;

    /**
     * 是否驳回申请人
     */
    @TableField("is_reject_first")
    private Boolean rejectFirst;

    /**
     * 是否首节点
     */
    @TableField("is_first")
    private Boolean first;

    /**
     * 温馨提示
     */
    private String tips;

    /**
     * 是否支持移动设备审批
     */
    @TableField("is_mobile")
    private Boolean mobile;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    private String modifierId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否可用
     */
    private Boolean yn;

    @TableField(exist = false)
    private String engineKey;

}
