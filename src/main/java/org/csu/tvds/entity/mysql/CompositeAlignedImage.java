package org.csu.tvds.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName composite_aligned_image
 */
@TableName(value = "composite_aligned_image")
@Data
public class CompositeAlignedImage implements Serializable {
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
    @TableField(value = "inspectionSeq")
    private Integer inspectionSeq;

    /**
     *
     */
    @TableField(value = "cameraNumber")
    private Integer cameraNumber;

    /**
     *
     */
    @TableField(value = "carriageId")
    private Integer carriageId;

    /**
     *
     */
    @TableField(value = "carriageNo")
    private Integer carriageNo;

    /**
     *
     */
    @TableField(value = "model")
    private String model;

    /**
     *
     */
    @TableField(value = "status")
    private Integer status;

    /**
     *
     */
    @TableField(value = "compositeUrl")
    private String compositeUrl;

    /**
     *
     */
    @TableField(value = "compositeTime")
    private LocalDateTime compositeTime;

    /**
     *
     */
    @TableField(value = "alignedUrl")
    private String alignedUrl;

    /**
     *
     */
    @TableField(value = "alignTime")
    private LocalDateTime alignTime;

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
        CompositeAlignedImage other = (CompositeAlignedImage) that;
        return (this.getDbId() == null ? other.getDbId() == null : this.getDbId().equals(other.getDbId()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getInspectionSeq() == null ? other.getInspectionSeq() == null : this.getInspectionSeq().equals(other.getInspectionSeq()))
                && (this.getCameraNumber() == null ? other.getCameraNumber() == null : this.getCameraNumber().equals(other.getCameraNumber()))
                && (this.getCarriageId() == null ? other.getCarriageId() == null : this.getCarriageId().equals(other.getCarriageId()))
                && (this.getCarriageNo() == null ? other.getCarriageNo() == null : this.getCarriageNo().equals(other.getCarriageNo()))
                && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCompositeUrl() == null ? other.getCompositeUrl() == null : this.getCompositeUrl().equals(other.getCompositeUrl()))
                && (this.getCompositeTime() == null ? other.getCompositeTime() == null : this.getCompositeTime().equals(other.getCompositeTime()))
                && (this.getAlignedUrl() == null ? other.getAlignedUrl() == null : this.getAlignedUrl().equals(other.getAlignedUrl()))
                && (this.getAlignTime() == null ? other.getAlignTime() == null : this.getAlignTime().equals(other.getAlignTime()))
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
        result = prime * result + ((getInspectionSeq() == null) ? 0 : getInspectionSeq().hashCode());
        result = prime * result + ((getCameraNumber() == null) ? 0 : getCameraNumber().hashCode());
        result = prime * result + ((getCarriageId() == null) ? 0 : getCarriageId().hashCode());
        result = prime * result + ((getCarriageNo() == null) ? 0 : getCarriageNo().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCompositeUrl() == null) ? 0 : getCompositeUrl().hashCode());
        result = prime * result + ((getCompositeTime() == null) ? 0 : getCompositeTime().hashCode());
        result = prime * result + ((getAlignedUrl() == null) ? 0 : getAlignedUrl().hashCode());
        result = prime * result + ((getAlignTime() == null) ? 0 : getAlignTime().hashCode());
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
        sb.append(", inspectionSeq=").append(inspectionSeq);
        sb.append(", cameraNumber=").append(cameraNumber);
        sb.append(", carriageId=").append(carriageId);
        sb.append(", carriageNo=").append(carriageNo);
        sb.append(", model=").append(model);
        sb.append(", status=").append(status);
        sb.append(", compositeUrl=").append(compositeUrl);
        sb.append(", compositeTime=").append(compositeTime);
        sb.append(", alignedUrl=").append(alignedUrl);
        sb.append(", alignTime=").append(alignTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
