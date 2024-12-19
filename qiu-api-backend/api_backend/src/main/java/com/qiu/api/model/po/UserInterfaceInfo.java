package com.qiu.api.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户接口调用关系
 * </p>
 *
 * @author qiu
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_interface_info")
@ApiModel(value="UserInterfaceInfo对象", description="用户接口调用关系")
public class UserInterfaceInfo implements Serializable {

    private static final long serialVersionUID = -9048415032532098501L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "接口id")
    private Long interfaceId;

    @ApiModelProperty(value = "总调用次数")
    private Integer totalNum;

    @ApiModelProperty(value = "剩余调用次数")
    private Integer leftNum;

    @ApiModelProperty(value = "调用状态 （0-正常，1-禁用）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 0 1（默认值0）")
    private Integer deleted;


}
