package gameClient;

import api.*;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Agent {
    private int _id;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private double _value;
    private List<node_data> _path;

    //
    public Agent(directed_weighted_graph gg, int start_node) {
        this._gg = gg;
        setMoney(0);
        setSpeed(0);
        this._curr_node = this._gg.getNode(start_node);
        this._pos = _curr_node.getLocation();
        this._id = -1;
        this._path = new LinkedList<>();
    }

    /**
     * This returns the next node the agent should go to
     *
     * @return
     */
    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    /**
     * This function updates the information about the agents from a string in the format of Jason
     *
     * @param json
     */
    public void updateAgents(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject ttt = line.getJSONObject("Agent");
            int id = ttt.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = ttt.getDouble("speed");
                String p = ttt.getString("pos");
                GeoLocation pp = new GeoLocation(p);
                int src = ttt.getInt("src");
                int dest = ttt.getInt("dest");


                double value = ttt.getDouble("value");
                this._pos = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(this._path, dest);
                this.setMoney(value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function set the currently edge of the agent
     *
     * @return _curr_edge
     */
    public edge_data get_curr_edge() {
        return this._curr_edge;
    }

    /**
     * Returns the ID of the agent
     *
     * @return _id
     */
    public int getID() {
        return _id;
    }

    /**
     * Returns the value node of the agent
     *
     * @return _value
     */
    public double getValue() {
        // TODO Auto-generated method stub
        return this._value;
    }

    /**
     * Returns the currently node of the agent
     *
     * @return _curr_node
     */
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    /**
     * This function set the currently node of the agent
     *
     * @param src
     */
    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    /**
     * This function set the speed of the agent
     *
     * @return
     */
    public void setSpeed(double v) {
        this._speed = v;
    }

    /**
     * Return the speed of the agent
     *
     * @return
     */
    public double getSpeed() {
        return this._speed;
    }

    /**
     * This function set the next node the agent should go to
     *
     * @param path
     * @param dest
     */
    private void setNextNode(List<node_data> path, int dest) {
        if (path.size() > 0) {
            if (dest == -1)
                setNextNode(path.remove(0).getKey());
        } else if (dest == -1) {
            setNextNode(-1);
        }
    }

    /**
     * Set the value (money)
     *
     * @param v
     */
    private void setMoney(double v) {
        _value = v;
    }

    /**
     * This function set the next node the agent should go to
     *
     * @param dest
     */
    private void setNextNode(int dest) {
        if (dest == -1) {
            _curr_edge = null;
        } else {
            this._curr_edge = _gg.getEdge(this.getSrcNode(), dest);
        }
    }

    /**
     * This function returns the position
     *
     * @return _pos
     */
    public geo_location getLocation() {
        return _pos;
    }

    /**
     * This function set the path for the agent
     *
     * @param path
     */
    public void setPath(List<node_data> path) {
        if (path.size() > 0)
            this.setNextNode(path, path.remove(0).getKey());
        this._path = path;
    }


    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return _id == agent._id &&
                Double.compare(agent._speed, _speed) == 0 &&
                Double.compare(agent._value, _value) == 0 &&
                Objects.equals(_pos, agent._pos) &&
                Objects.equals(_curr_edge, agent._curr_edge) &&
                Objects.equals(_curr_node, agent._curr_node) &&
                Objects.equals(_gg, agent._gg) &&
                Objects.equals(_path, agent._path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _pos, _speed, _curr_edge, _curr_node, _gg, _value, _path);
    }
}
