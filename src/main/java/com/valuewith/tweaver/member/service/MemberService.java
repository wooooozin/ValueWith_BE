package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.ChatRoom;
import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.user.User;

public interface MemberService {
  void setMember(Group group, User user, ChatRoom chatRoom);
}
