package com.vsu.cgcourse.obj_reader;

import com.vsu.cgcourse.model.Mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DeletePolygons {

    public static ArrayList<Integer> arrayOfPolygonsIndicesForDelete(String fileWithDeletePolygons) {
        ArrayList<Integer> polygonsIndices = new ArrayList<>();
        Scanner scanner = new Scanner(fileWithDeletePolygons);
        while (scanner.hasNextLine()) {
            polygonsIndices.add(Integer.parseInt(scanner.nextLine()));
        }
        return polygonsIndices;
    }

    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static Mesh readingAndDeletingPolygons(String fileContent, String fileWithDeletePolygons) {
        Mesh result = new Mesh();

        // вводим счетчик кол-ва вершин и нормалей в списках (test08)
        int countVertex = 0;
        int countTextureVertex = 0;
        int countNormal = 0;

        int lineInd = 0, faceInd = 0;

        Scanner scanner = new Scanner(fileContent);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            ArrayList<String> wordsInLine = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
            if (wordsInLine.isEmpty())
                continue;

            final String token = wordsInLine.get(0);
            wordsInLine.remove(0);

            ++lineInd;
            switch (token) {
                case OBJ_VERTEX_TOKEN:
                    result.vertices.add(ObjReader.parseVertex(wordsInLine, lineInd));
                    countVertex = result.vertices.size();
                    break;
                case OBJ_TEXTURE_TOKEN:
                    result.textureVertices.add(ObjReader.parseTextureVertex(wordsInLine, lineInd));
                    countTextureVertex = result.textureVertices.size();
                    break;
                case OBJ_NORMAL_TOKEN:
                    result.normals.add(ObjReader.parseNormal(wordsInLine, lineInd));
                    countNormal = result.normals.size();
                    break;
                case OBJ_FACE_TOKEN:
                    faceInd++;
                    ArrayList<Integer> onePolygonVertexIndices = new ArrayList<Integer>();
                    ArrayList<Integer> onePolygonTextureVertexIndices = new ArrayList<Integer>();
                    ArrayList<Integer> onePolygonNormalIndices = new ArrayList<Integer>();

                    if (!readDeleteFile(fileWithDeletePolygons).contains(faceInd)) {
                        ObjReader.parseFace(wordsInLine, onePolygonVertexIndices, onePolygonTextureVertexIndices, onePolygonNormalIndices,
                                lineInd, countVertex, countTextureVertex, countNormal);

                        result.polygonVertexIndices.add(onePolygonVertexIndices);
                        result.polygonTextureVertexIndices.add(onePolygonTextureVertexIndices);
                        result.polygonNormalIndices.add(onePolygonNormalIndices);
                        break;
                    }
                default: {}
            }
        }
        return result;
    }

    public static ArrayList<Integer> readDeleteFile(String fileWithDeletePolygons) {
        ArrayList<Integer> polygonsIndices = new ArrayList<>();
        Scanner scanner = new Scanner(fileWithDeletePolygons);
        while (scanner.hasNextLine()) {
            polygonsIndices.add(Integer.parseInt(scanner.nextLine()));
        }
        return polygonsIndices;
    }

//    public static ArrayList<ArrayList<String>> readDeleteFile2(String fileWithDeletePolygons) {
//        ArrayList<ArrayList<String>> allWordsInLine = new ArrayList<>();
//        Scanner scanner = new Scanner(fileWithDeletePolygons);
//        while (scanner.hasNextLine()) {
//            final String line = scanner.nextLine();
//            ArrayList<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
//            if (wordsInLine.isEmpty())
//                continue;
//            wordsInLine.remove(0);
//            allWordsInLine.add(wordsInLine);
//        }
//        return allWordsInLine;
//    }

    public static Mesh createDeleteArray(Mesh model, String deleteArray) {
        String str = String.valueOf(deleteArray);
        String[] strDeleteArr = str.split(" ");
        int[] numArr = new int[strDeleteArr.length];
        for (int i = 0; i < strDeleteArr.length; i++) {
            numArr[i] = Integer.parseInt(strDeleteArr[i]);
        }

        delete(model, numArr);

        return model;
    }

    public static void delete(Mesh model, int[] input) {
        for (int i = 0; i < input.length; i++) {
            model.polygonVertexIndices.remove(input[i]);
        }
        for (int i = 0; i < input.length; i++) {
            model.polygonTextureVertexIndices.remove(input[i]);
        }
        for (int i = 0; i < input.length; i++) {
            model.polygonNormalIndices.remove(input[i]);
        }

        for (int count = 0; count < model.vertices.size(); count++){
            int temp = 0;
            for(int i = 0 ;i < model.vertices.size(); i++){
                for(int j = 0; j<3; j++) {
                    if(count == model.polygonVertexIndices.get(i).get(j)){
                        temp++;
                    }
                }
                if(temp>0){
                    break;
                }
            }
            if(temp == 0){
                model.vertices.remove(count);
            }
        }
    }
}
