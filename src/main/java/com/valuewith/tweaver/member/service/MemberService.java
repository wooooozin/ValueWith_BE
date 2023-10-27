package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.Group;
import com.valuewith.tweaver.user.entity.User;

public interface MemberService {
  void setMember(Group group, User user, ChatRoom chatRoom);
}
