package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.user.entity.User;

public interface MemberService {
  void setMember(TripGroup tripGroup, User user, ChatRoom chatRoom);
}
