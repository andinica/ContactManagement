package eu.ase.ro.ContactManagement.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class GroupWithMemberCount {
    @Embedded
    private Group group;

    public GroupWithMemberCount(Group group, int memberCount) {
        this.group = group;
        this.memberCount = memberCount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    private int memberCount;
}
