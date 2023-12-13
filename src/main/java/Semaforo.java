public class Semaforo{
		private String id;
		private int[] fases;
		private int offset;
		
		public Semaforo(String id, int[] fases, int offset) {
			this.setId(id);
			this.setFases(fases);
			this.setOffset(offset);
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int[] getFases() {
			return fases;
		}

		public void setFases(int[] fases) {
			this.fases = fases;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}
	}
