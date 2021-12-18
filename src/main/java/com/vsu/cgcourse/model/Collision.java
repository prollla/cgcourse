package com.vsu.cgcourse.model;


import com.vsu.cgcourse.math.utils.Vector3f;
import com.vsu.cgcourse.math.utils.VectorUtils;

public class Collision {
    public static boolean testInterSection(Mesh mesh, Mesh secondMesh) {
        return mesh.matrix[0][3].equals(secondMesh.matrix[0][3]) && secondMesh.matrix[2][3].equals(mesh.matrix[2][3]);
    }
}
