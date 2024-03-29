package org.csu.tvds.entity.mysql;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName job_assign
 */
@TableName(value = "job_assign")
@Data
public class JobAssign implements Serializable {
    /**
     *
     */
    @TableId(value = "dbId")
    @TableField(value = "dbId")
    private Long dbId;

    /**
     *
     */
    @TableField(value = "personnelSeq")
    private String personnelSeq;

    /**
     *
     */
    @TableField(value = "assignee")
    private Long assignee;

    /**
     *
     */
    @TableField(value = "deadline")
    private LocalDateTime deadline;

    /**
     *
     */
    @TableField(value = "targetCarriage")
    private Long targetCarriage;

    /**
     *
     */
    @TableField(value = "comment")
    private String comment;

    /**
     *
     */
    @TableField(value = "status")
    private Integer status;

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
        JobAssign other = (JobAssign) that;
        return (this.getDbId() == null ? other.getDbId() == null : this.getDbId().equals(other.getDbId()))
                && (this.getPersonnelSeq() == null ? other.getPersonnelSeq() == null : this.getPersonnelSeq().equals(other.getPersonnelSeq()))
                && (this.getAssignee() == null ? other.getAssignee() == null : this.getAssignee().equals(other.getAssignee()))
                && (this.getDeadline() == null ? other.getDeadline() == null : this.getDeadline().equals(other.getDeadline()))
                && (this.getTargetCarriage() == null ? other.getTargetCarriage() == null : this.getTargetCarriage().equals(other.getTargetCarriage()))
                && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getIsDeleted() == null ? other.getIsDeleted() == null : this.getIsDeleted().equals(other.getIsDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDbId() == null) ? 0 : getDbId().hashCode());
        result = prime * result + ((getPersonnelSeq() == null) ? 0 : getPersonnelSeq().hashCode());
        result = prime * result + ((getAssignee() == null) ? 0 : getAssignee().hashCode());
        result = prime * result + ((getDeadline() == null) ? 0 : getDeadline().hashCode());
        result = prime * result + ((getTargetCarriage() == null) ? 0 : getTargetCarriage().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", personnelSeq=").append(personnelSeq);
        sb.append(", assignee=").append(assignee);
        sb.append(", deadline=").append(deadline);
        sb.append(", targetCarriage=").append(targetCarriage);
        sb.append(", comment=").append(comment);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
