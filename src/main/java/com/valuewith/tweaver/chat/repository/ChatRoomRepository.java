package com.valuewith.tweaver.chat.repository;


import com.valuewith.tweaver.chat.entity.ChatRoom;
import com.valuewith.tweaver.group.entity.TripGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  Optional<ChatRoom> findByTripGroup(TripGroup tripGroup);
  Optional<ChatRoom> findByTripGroupTripGroupId(Long tripGroupId);
  void deleteByTripGroupTripGroupId(Long tripGroupId);
  List<ChatRoom> findChatRoomsByTripGroup_TripGroupId(Long tripGroupId);
}
