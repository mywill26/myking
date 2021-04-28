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
 * 流程角色
 * </p>
 *
 * @author mycx26
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_proc_role")
public class ProcRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色code
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 顺序号
     */
    private Integer orderNo;

    /**
     * 描述
     */
    private String description;

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
     * 可用标记
     */
    private Boolean yn;

}
