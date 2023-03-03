package org.csu.tvds.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName templates_lib
 */
@TableName(value = "templates_lib")
@Data
public class TemplatesLib implements Serializable {
    /**
     *
     */
    @TableId(value = "dbId")
    private Long dbId;

    /**
     *
     */
    @TableField(value = "id")
    private String id;

    /**
     *
     */
    @TableField(value = "model")
    private String model;

    /**
     *
     */
    @TableField(value = "cameraNumber")
    private Integer cameraNumber;

    /**
     *
     */
    @TableField(value = "templateUrl")
    private String templateUrl;

    /**
     *
     */
    @TableField(value = "createYear")
    private Integer createYear;

    /**
     *
     */
    @TableField(value = "version")
    private String version;

    /**
     *
     */
    @TableField(value = "createTime")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(value = "updateTime")
    private LocalDateTime updateTime;

    /**
     *
     */
    @TableField(value = "isDeleted")
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TemplatesLib other = (TemplatesLib) that;
        return (this.getDbId() == null ? other.getDbId() == null : this.getDbId().equals(other.getDbId()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
                && (this.getCameraNumber() == null ? other.getCameraNumber() == null : this.getCameraNumber().equals(other.getCameraNumber()))
                && (this.getTemplateUrl() == null ? other.getTemplateUrl() == null : this.getTemplateUrl().equals(other.getTemplateUrl()))
                && (this.getCreateYear() == null ? other.getCreateYear() == null : this.getCreateYear().equals(other.getCreateYear()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDbId() == null) ? 0 : getDbId().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getCameraNumber() == null) ? 0 : getCameraNumber().hashCode());
        result = prime * result + ((getTemplateUrl() == null) ? 0 : getTemplateUrl().hashCode());
        result = prime * result + ((getCreateYear() == null) ? 0 : getCreateYear().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDeleted() == null) ? 0 : getIsDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dbId=").append(dbId);
        sb.append(", id=").append(id);
        sb.append(", model=").append(model);
        sb.append(", cameraNumber=").append(cameraNumber);
        sb.append(", templateUrl=").append(templateUrl);
        sb.append(", createYear=").append(createYear);
        sb.append(", version=").append(version);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
