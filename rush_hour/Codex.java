ackage rush_hour;

import java.util.Arrays;

public class Codex implements Comparable<Codex>{
    private long[] code = new long[2];

    public Codex(State self){
        this(self.get_code());
    }

    public Codex(long[] code) {
        this.code = Arrays.copyOf(code, code.length);
    }

    public Codex(Codex x) {
        this(x.code);
    }

    public Codex() {
        this.code = null;
    }

    public long get_code_h() {
        return this.code[0];
    }

    public long get_code_v() {
        return this.code[1];
    }

    public void update_h(long delta_h) {
        this.code[0] += delta_h;
    }

    public void update_v(long delta_v) {
        this.code[1]+=delta_v;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.code);
    }

    @Override
    public int compareTo(Codex other) {
        if(this.get_code_h() > other.get_code_h())
            return 1;
        else if(this.get_code_h() < other.get_code_h())
            return -1;
        else {
            if(this.get_code_v() > other.get_code_v())
                return 1;
            else if(this.get_code_v() < other.get_code_v())
                return -1;
            else return 0;
        }
    }//Can't use this.code - other.code because of overflow

    @Override
    public boolean equals(Object other) {
        return (this.compareTo((Codex)other) == 0);
    }
}
