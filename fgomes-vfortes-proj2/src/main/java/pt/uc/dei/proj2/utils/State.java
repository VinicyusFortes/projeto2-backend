package pt.uc.dei.proj2.utils;

public enum State {
  RASCUNHO(1),
  DISPONIVEL(2),
  RESERVADO(3),
  COMPRADO(4);

  private final int stateId;

  State(int stateId) {
    this.stateId = stateId;
  }

  public int getStateId() {
    return stateId;
  }

  public static State fromId(int id) {
    for (State state : State.values()) {
      if (state.getStateId() == id) {
        return state;
      }
    }
    throw new IllegalArgumentException("ID de estado inválido: " + id);
  }
}
