package com.mycx26.base.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 流程节点变量映射
 * </p>
 *
 * @author mycx26
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_node_var")
public class ProcNodeVar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程定义key
     */
    private String procDefKey;

    /**
     * 节点key
     */
    private String nodeKey;

    /**
     * 变量名
     */
    private String varName;

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

}
