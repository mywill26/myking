package com.mycx26.base.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 流程关联关系
 * </p>
 *
 * @author mycx26
 * @since 2020-06-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_relation")
public class ProcRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 流程实例id1
     */
    private String procInstId1;

    /**
     * 流水号1
     */
    private String flowNo1;

    /**
     * 流程定义key1
     */
    private String procDefKey1;

    /**
     * 流程实例id2
     */
    private String procInstId2;

    /**
     * 流水号2
     */
    private String flowNo2;

    /**
     * 流程定义key2
     */
    private String procDefKey2;

}
