package com.valuewith.tweaver.member.service;

import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.user.entity.Member;

public interface MemberService {
  void setMember(TripGroup tripGroup, Member member, ChatRoom chatRoom);
}
