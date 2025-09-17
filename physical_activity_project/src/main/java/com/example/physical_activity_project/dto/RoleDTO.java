package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String name;
    private String description;
    private List<Long> permissionIds;  // IDs de permisos asociados
}
