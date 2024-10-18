package com.sohan.user_service.service;

import com.sohan.user_service.dto.request.PermissionRequest;
import com.sohan.user_service.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {

    PermissionResponse create(PermissionRequest request);

    List<PermissionResponse> getAll();

    boolean delete(String name);

    PermissionResponse getById(String name);
}
