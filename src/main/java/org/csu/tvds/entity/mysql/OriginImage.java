package org.csu.tvds.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName origin_image
 */
@TableName(value = "origin_image")
@Data
public class OriginImage implements Serializable {
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
    @TableField(value = "filename")
    private String filename;

    /**
     *
     */
    @TableField(value = "inspectionSeqDay")
    private Integer inspectionSeqDay;

    /**
     *
     */
    @TableField(value = "localUrl")
    private String localUrl;

    /**
     *
     */
    @TableField(value = "cameraNumber")
    private Integer cameraNumber;

    /**
     *
     */
    @TableField(value = "carriageNumber")
    private Integer carriageNumber;

    /**
     *
     */
    @TableField(value = "contentType")
    private String contentType;

    /**
     *
     */
    @TableField(value = "relatedId")
    private String relatedId;

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
        OriginImage other = (OriginImage) that;
        return (this.getDbId() == null ? other.getDbId() == null : this.getDbId().equals(other.getDbId()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFilename() == null ? other.getFilename() == null : this.getFilename().equals(other.getFilename()))
                && (this.getInspectionSeqDay() == null ? other.getInspectionSeqDay() == null : this.getInspectionSeqDay().equals(other.getInspectionSeqDay()))
                && (this.getLocalUrl() == null ? other.getLocalUrl() == null : this.getLocalUrl().equals(other.getLocalUrl()))
                && (this.getCameraNumber() == null ? other.getCameraNumber() == null : this.getCameraNumber().equals(other.getCameraNumber()))
                && (this.getCarriageNumber() == null ? other.getCarriageNumber() == null : this.getCarriageNumber().equals(other.getCarriageNumber()))
                && (this.getContentType() == null ? other.getContentType() == null : this.getContentType().equals(other.getContentType()))
                && (this.getRelatedId() == null ? other.getRelatedId() == null : this.getRelatedId().equals(other.getRelatedId()))
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
        result = prime * result + ((getFilename() == null) ? 0 : getFilename().hashCode());
        result = prime * result + ((getInspectionSeqDay() == null) ? 0 : getInspectionSeqDay().hashCode());
        result = prime * result + ((getLocalUrl() == null) ? 0 : getLocalUrl().hashCode());
        result = prime * result + ((getCameraNumber() == null) ? 0 : getCameraNumber().hashCode());
        result = prime * result + ((getCarriageNumber() == null) ? 0 : getCarriageNumber().hashCode());
        result = prime * result + ((getContentType() == null) ? 0 : getContentType().hashCode());
        result = prime * result + ((getRelatedId() == null) ? 0 : getRelatedId().hashCode());
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
        sb.append(", filename=").append(filename);
        sb.append(", inspectionSeqDay=").append(inspectionSeqDay);
        sb.append(", localUrl=").append(localUrl);
        sb.append(", cameraNumber=").append(cameraNumber);
        sb.append(", carriageNumber=").append(carriageNumber);
        sb.append(", contentType=").append(contentType);
        sb.append(", relatedId=").append(relatedId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
