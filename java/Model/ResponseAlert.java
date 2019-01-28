package Model;

public class ResponseAlert {
    private boolean response;

    public ResponseAlert() {
    }

    public ResponseAlert(boolean response) {
        this.response = response;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
