package org.csu.tvds.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName part_info
 */
@TableName(value = "part_info")
@Data
public class PartInfo implements Serializable {
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
    @TableField(value = "partName")
    private String partName;

    /**
     *
     */
    @TableField(value = "carriageNo")
    private Integer carriageNo;

    /**
     *
     */
    @TableField(value = "inspectionSeq")
    private Integer inspectionSeq;

    /**
     *
     */
    @TableField(value = "model")
    private String model;

    /**
     *
     */
    @TableField(value = "compositeId")
    private String compositeId;

    /**
     *
     */
    @TableField(value = "imageUrl")
    private String imageUrl;

    /**
     *
     */
    @TableField(value = "status")
    private Integer status;

    /**
     *
     */
    @TableField(value = "verifyStatusA")
    private Integer verifyStatusA;

    /**
     *
     */
    @TableField(value = "verifyStatusB")
    private Integer verifyStatusB;


    /**
     *
     */
    @TableField(value = "commentA")
    private String commentA;

    /**
     *
     */
    @TableField(value = "commentB")
    private String commentB;

    /**
     *
     */
    @TableField(value = "hasConflict")
    private Boolean hasConflict;

    /**
     *
     */
    @TableField(value = "checkTime")
    private LocalDateTime checkTime;

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
    private static final long serialVersionUID = 800936161622880456L;

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
        PartInfo other = (PartInfo) that;
        return (this.getDbId() == null ? other.getDbId() == null : this.getDbId().equals(other.getDbId()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getPartName() == null ? other.getPartName() == null : this.getPartName().equals(other.getPartName()))
                && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
                && (this.getCompositeId() == null ? other.getCompositeId() == null : this.getCompositeId().equals(other.getCompositeId()))
                && (this.getImageUrl() == null ? other.getImageUrl() == null : this.getImageUrl().equals(other.getImageUrl()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getVerifyStatusA() == null ? other.getVerifyStatusA() == null : this.getVerifyStatusA().equals(other.getVerifyStatusA()))
                && (this.getVerifyStatusB() == null ? other.getVerifyStatusB() == null : this.getVerifyStatusB().equals(other.getVerifyStatusB()))
                && (this.getCommentA() == null ? other.getCommentA() == null : this.getCommentA().equals(other.getCommentA()))
                && (this.getCommentB() == null ? other.getCommentB() == null : this.getCommentB().equals(other.getCommentB()))
                && (this.getHasConflict() == null ? other.getHasConflict() == null : this.getHasConflict().equals(other.getHasConflict()))
                && (this.getCheckTime() == null ? other.getCheckTime() == null : this.getCheckTime().equals(other.getCheckTime()))
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
        result = prime * result + ((getPartName() == null) ? 0 : getPartName().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getCompositeId() == null) ? 0 : getCompositeId().hashCode());
        result = prime * result + ((getImageUrl() == null) ? 0 : getImageUrl().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getVerifyStatusA() == null) ? 0 : getVerifyStatusA().hashCode());
        result = prime * result + ((getVerifyStatusB() == null) ? 0 : getVerifyStatusB().hashCode());
        result = prime * result + ((getCommentA() == null) ? 0 : getCommentA().hashCode());
        result = prime * result + ((getCommentB() == null) ? 0 : getCommentB().hashCode());
        result = prime * result + ((getHasConflict() == null) ? 0 : getHasConflict().hashCode());
        result = prime * result + ((getCheckTime() == null) ? 0 : getCheckTime().hashCode());
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
        sb.append(", partName=").append(partName);
        sb.append(", model=").append(model);
        sb.append(", compositeId=").append(compositeId);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", status=").append(status);
        sb.append(", verifyStatusA=").append(verifyStatusA);
        sb.append(", verifyStatusB=").append(verifyStatusB);
        sb.append(", commentA=").append(commentA);
        sb.append(", commentB=").append(commentB);
        sb.append(", hasConflict=").append(hasConflict);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
