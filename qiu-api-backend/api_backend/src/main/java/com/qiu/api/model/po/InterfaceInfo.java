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
 * 接口信息
 * </p>
 *
 * @author qiu
 * @since 2024-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("interface_info")
@ApiModel(value="InterfaceInfo", description="接口信息")
public class InterfaceInfo implements Serializable {

    private static final long serialVersionUID = -940740191887772749L;

    @ApiModelProperty(value = "接口id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "服务器地址")
    private String host;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "请求参数")
    private String requestParams;

    @ApiModelProperty(value = "请求头")
    private String requestHeader;

    @ApiModelProperty(value = "响应头")
    private String responseHeader;

    @ApiModelProperty(value = "接口状态 （0-关闭，1-开启）")
    private Integer status;

    @ApiModelProperty(value = "请求类型")
    private String method;

    @ApiModelProperty(value = "创建人")
    private Long userId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除 0 1（默认值0）")
    private Integer deleted;


}
