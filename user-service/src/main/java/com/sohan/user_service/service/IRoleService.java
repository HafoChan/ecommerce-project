package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.RoleRequest;
import com.sohan.user_service.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {

    RoleResponse create(RoleRequest request);

    List<RoleResponse> getAll();

    boolean delete(String name);

    RoleResponse getById(String name);
}
