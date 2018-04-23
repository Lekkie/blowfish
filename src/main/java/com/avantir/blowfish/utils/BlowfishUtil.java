package com.avantir.blowfish.utils;

import javafx.collections.transformation.SortedList;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lekanomotayo on 12/01/2018.
 */
public class BlowfishUtil {


    public static void main(String[] args){

        /*
        BlowfishUtil.possibilities(".");
        BlowfishUtil.possibilities(".-");
        BlowfishUtil.possibilities("?");
        BlowfishUtil.possibilities("?.");
        BlowfishUtil.possibilities(".?");

        BlowfishUtil.possibilities("?..");
        BlowfishUtil.possibilities("??.");
        BlowfishUtil.possibilities("???");
        */

        BlowfishUtil.sjf(Arrays.asList(30, 3,10,10,20,1,2),3);

        //System.out.println(2000 >> 1);
    }


    private static class JobOrder{

        public static final transient Comparator<JobOrder> BY_CC_TIME = new ByCCTime();

        private Integer order;
        private Integer ccTime;

        public JobOrder(Integer order, Integer ccTime){
            this.ccTime = ccTime;
            this.order = order;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JobOrder jobOrder = (JobOrder) o;

            if (ccTime != null ? !ccTime.equals(jobOrder.ccTime) : jobOrder.ccTime != null) return false;
            return order != null ? order.equals(jobOrder.order) : jobOrder.order == null;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (order != null ? order.hashCode() : 0);
            result = 31 * result + (ccTime != null ? ccTime.hashCode() : 0);
            return result;
        }

        private static class ByCCTime implements Comparator<JobOrder> {
            public int compare(JobOrder v, JobOrder w){
                int a = v.order;
                int b = w.order;

                int x = v.ccTime;
                int y = w.ccTime;
                return (x < y) ? -1 : ((x == y) ? ((a < b) ? -1 : ((a == b) ? 0 : 1)) : 1);
            }
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public Integer getCcTime() {
            return ccTime;
        }

        public void setCcTime(Integer ccTime) {
            this.ccTime = ccTime;
        }
    }

    public static Integer sjf(List<Integer> jobs, Integer index ) {

        List<JobOrder> jobOrderList = new ArrayList<>();
        int i = 0;
        for(Integer job: jobs){
            jobOrderList.add(new JobOrder(i++, job));
        }

        List<JobOrder> orderedJob = jobOrderList.stream()
                .sorted((a, b)  -> JobOrder.BY_CC_TIME.compare(a, b))
                .collect(Collectors.toList());

        System.out.println(orderedJob);
        int cc = 0;
        for(JobOrder jobOrder: orderedJob){
            if(jobOrder.getOrder() == index)
            {
                System.out.println(cc);
                return cc;
            }
            //executeJob()
            cc += jobOrder.getCcTime();
        }

        System.out.println(cc);
        return cc;
    }


    private static Node root = new Node();
    static{
        buildBinaryTree();
    }

    public static List<String> possibilities(String word ) {
        List test = convertMorseCode2Letter(word);
        System.out.println(test);
        return test;
    }


    private static List<String> convertMorseCode2Letter(String morse){
        List<String> possibleStrList = new ArrayList();
        List<Node> possbileRootList = new ArrayList();
        possbileRootList.add(root);
        return convertMorseCode2Letter(morse, possibleStrList, possbileRootList, 0);
    }

    private static List<String> convertMorseCode2Letter(String morse, List<String> possibleGeneratedStringList, List<Node> currentRootNodeList, int index){

        if (index == morse.length()){
            return possibleGeneratedStringList;
        }
        else{
            char currentChar = morse.charAt(index);
            List<Node> possibleNextNodeList = new ArrayList();
            for(Node node: currentRootNodeList){
                possibleNextNodeList = next(node, currentChar);
                for(Node nodeTmp: possibleNextNodeList){
                    List<Node> nextNodeTmpList = new ArrayList();
                    nextNodeTmpList.add(nodeTmp);
                    if ((morse.length() - 1) == index)
                        possibleGeneratedStringList.add(String.valueOf(nodeTmp.getLetter()));
                    convertMorseCode2Letter(morse, possibleGeneratedStringList, nextNodeTmpList, index + 1);
                }
            }
            return possibleGeneratedStringList;
        }
    }

    private static List<Node> next(Node current, char d) {
        switch (d) {
            case '.':
                if (current.getLeft() == null)
                    current.setLeft(new Node());
                List possiblitiesLeft = new ArrayList();
                possiblitiesLeft.add(current.getLeft());
                return possiblitiesLeft;

            case '-':
                if (current.getRight() == null)
                    current.setRight(new Node());
                List possiblitiesRight = new ArrayList();
                possiblitiesRight.add(current.getRight());
                return possiblitiesRight;
            case '?':
                if (current.getRight() == null)
                    current.setRight(new Node());
                if (current.getLeft() == null)
                    current.setLeft(new Node());
                List possiblities = new ArrayList();
                possiblities.add(current.getLeft());
                possiblities.add(current.getRight());
                return possiblities;
            default:
                return null;
        }
    }

    private static void add(char letter, String morse) {
        Node current = root;
        for (int i = 0; i < morse.length(); i++) {
            List<Node> nextNodeList = next(current, morse.charAt(i));
            current = nextNodeList.get(0);
        }
        current.setLetter(letter);
        current.setMorse(morse);
    }


    private static class Node{

        private char letter;
        private String morse;
        private Node left;
        private Node right;

        public Node() {
            letter = '\b';
            left = null;
            right = null;
        }

        public void setLetter(char letter) {
            this.letter = letter;
        }

        public char getLetter(){
            return letter;
        }

        public void setMorse(String morse) {
            this.morse = morse;
        }

        public String getMorse() {
            return morse;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getLeft() {
            return left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getRight() {
            return right;
        }
    }


    private static void buildBinaryTree() {
        add('A', ".-");
        add('B', "-...");
        add('C', "-.-.");
        add('D', "-..");
        add('E', ".");
        add('F', "..-.");
        add('G', "--.");
        add('H', "....");
        add('I', "..");
        add('J', ".---");
        add('K', "-.-");
        add('L', ".-..");
        add('M', "--");
        add('N', "-.");
        add('O', "---");
        add('P', ".--.");
        add('Q', "--.-");
        add('R', ".-.");
        add('S', "...");
        add('T', "-");
        add('U', "..-");
        add('V', "...-");
        add('W', ".--");
        add('X', "-..-");
        add('Y', "-.--");
        add('Z',"--..");
        add('Ü',"..--");
        add('Ä', ".-.-");
        add('Ö', "---.");
        //add('CH', "----");
        add('5', ".....");
        add('4', "....-");
        add('Ŝ', "...-.");
        add('3', "...--");
        add('É', "..-..");
        add('Đ', "..--.");
        add('2', "..---");
        add('È', ".-..-");
        add('+', ".-.-.");
        add('Þ', ".--..");
        add('À', ".--.-");
        add('Ĵ', ".---.");
        add('1', ".----");
        add('6', "-....");
        add('=', "-...-");
        add('/', "-..-.");
        add('Ç', "-.-..");
        add('Ĥ', "-.--.");
        add('7', "--...");
        add('Ĝ', "--.-.");
        add('Ñ', "--.--");
        add('8', "---..");
        add('9', "----.");
        add('0', "-----");
        add('?', "..--..");
        add('_', "..--.-");
        add('"', ".-..-.");
        add('.', ".-.-.-");
        add('@', ".--.-.");
        add('\'', ".----.");
        add('-', "-....-");
        add('\b', "-.-.-");
        add(';', "-.-.-.");
        add('!', "-.-.--");
        //add('()', "-.--.-");
        add('\b', "--..-");
        add(',', "--..--");
        add(':', "---...");
    }
}
