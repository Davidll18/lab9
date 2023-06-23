package pe.edu.pucp.tel131lab9.dto;

import pe.edu.pucp.tel131lab9.bean.Employee;

public class PostDTO extends Employee {
    private int cant_post;
    private int post_id;

    public int getCant_post() {
        return cant_post;
    }

    public void setCant_post(int cant_post) {
        this.cant_post = cant_post;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
