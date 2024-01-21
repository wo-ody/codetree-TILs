import java.io.*;
import java.util.*;

public class Main {
    static int Q,N,M;

    static int[][] belt;
    static long[][] box;
    static boolean[] box_not;
    static boolean[] belt_not;
    static HashMap<Integer,Integer> numbering = new HashMap<>();

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        Q = Integer.parseInt(br.readLine());
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int tc = Integer.parseInt(st.nextToken());

            switch (tc){
                case 100 :
                    fun_100(st);
                    break;
                case 200 :
                    System.out.println(fun_200(st));
                    break;
                case 300 :
                    System.out.println(fun_300(st));
                    break;
                case 400 :
                    System.out.println(fun_400(st));
                    break;
                case 500 :
                    System.out.println(fun_500(st));
                    break;
            }
        }
        //System.out.println(1);
    }

    static void fun_100(StringTokenizer st){
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        belt = new int[M+1][2];
        box = new long[N+1][5];
        box_not = new boolean[N+1];
        belt_not = new boolean[M+1];

        int cnt = 0;
        int bnum = 1;
        for (int i = 0; i < N; i++) {
            int real_num = Integer.parseInt(st.nextToken());
            numbering.put(real_num,i+1);
            box[i+1][4] = real_num;
            box[i+1][0] = i;
            box[i][1] = i+1;
            box[i+1][3] = bnum;
            cnt++;
            if(cnt == 1){
                box[i+1][0] = 0;
                box[i][1] = 0;
                belt[bnum][0] = i+1;
            }
            else if(cnt == N/M) {
                belt[bnum][1] = i+1;
                cnt = 0;
                bnum++;
            }
        }
        for (int i = 0; i < N; i++) {
            box[i+1][2] = Integer.parseInt(st.nextToken());
        }
    }
    static long fun_200(StringTokenizer st){
        int w_max = Integer.parseInt(st.nextToken());

        long result = 0;

        for (int i = 1; i <= M ; i++) {
            // 변경 - belt 앞,뒤 / box 앞,뒤
            // w_max 이하, 하차
            int front_id = belt[i][0];
            if(belt_not[i] || front_id == 0) continue; // 부서진 belt, 벨트위에 아무것도없음
            if (box[front_id][2] <= w_max) {
                result += box[front_id][2];

                int back_id = (int)box[front_id][1];
                if(back_id == 0){
                    belt[i][0] = 0;
                    belt[i][1] = 0;
                    box_not[front_id] = true;
                }
                else {
                    // box
                    box[back_id][0] = 0;
                    box_not[front_id] = true;

                    // belt
                    belt[i][0] = back_id;
                }
            }
            // 뒤로 보내기
            else {
                int back_id = belt[i][1];
                if(belt[i][0] == belt[i][1]) continue;
                //belt
                belt[i][0] = (int)box[front_id][1];
                belt[i][1] = front_id;

                //box
                box[back_id][1] = front_id;
                box[front_id][0] = back_id;
                box[front_id][1] = 0;
                box[belt[i][0]][0] = 0;
            }
        }
        return result;
    }

    static int fun_300(StringTokenizer st){
        int r_id = Integer.parseInt(st.nextToken());
        if(!numbering.containsKey(r_id)) return -1;

        int id = numbering.get(r_id);// 내가 부여한 id
        if(box_not[id]) return -1;

        int front_id = (int)box[id][0];
        int back_id = (int)box[id][1];
        int belt_num = (int)box[id][3];

        // box
        box[front_id][1] = back_id;
        box[back_id][0] = front_id;
        box_not[id] = true;

        // belt
        if(belt[belt_num][0] == id) {
            belt[belt_num][0] = back_id;
        }
        if(belt[belt_num][1] == id) belt[belt_num][1] = front_id;
        
        return r_id;
    }

    static int fun_400(StringTokenizer st){
        int f_id = Integer.parseInt(st.nextToken());
        if(!numbering.containsKey(f_id)) return -1;
        int id = numbering.get(f_id);

        if(box_not[id]) return -1;

        int belt_num = (int)box[id][3];
        int original_front_id = belt[belt_num][0];
        int original_back_id = belt[belt_num][1];
        int box_front_id = (int)box[id][0]; // 이어질 부분

        // belt
        belt[belt_num][0] = id;
        belt[belt_num][1] = box_front_id;
        // box
        box[box_front_id][1] = 0;
        box[id][0] = 0;
        box[original_back_id][1] = original_front_id;
        box[original_front_id][0] = original_back_id;

        return belt_num;
    }

    static int fun_500(StringTokenizer st){
        int b_num = Integer.parseInt(st.nextToken());

        if(belt_not[b_num]) return -1;
        else {
            // b_num 오른쪽 벨트부터 봐서
            // 망가지지 않은 벨트위에 상자들을 놓기 ( 최소 하나 이상이 정상 상태 )
            belt_not[b_num] = true;
            int broken_front_id = belt[b_num][0];
            int broken_back_id = belt[b_num][1];
            int move_belt = b_num;
            while (true) {
                move_belt++;
                if(move_belt > M) move_belt = 1;
                if (belt_not[move_belt]) continue;
                else {
                    break;
                }
            }
            int back_id = belt[move_belt][1];
            // box
            box[back_id][1] = broken_front_id;
            box[broken_front_id][0] = back_id;

            // belt
            belt[move_belt][1] = broken_back_id;
            int fid = broken_front_id;
            while(fid != 0){
                box[fid][3] = move_belt;
                fid = (int)box[fid][1];
            }

            return b_num;
        }
    }
}