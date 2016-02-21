package com.interaxon.test.libmuse;
/**
 * Implementa un vector circular
 * 
 * @author victor.ferrer
 * 
 */
public class CircularQueue {

	private int size;
	private float[] queue;
	private int posLastItem;

	/**
	 * 
	 * @param size
	 *            Tama�o de la cola circular
	 */
	public CircularQueue(int size) {
		this.size = size;
		queue = new float[size];
		posLastItem = size - 1;
	}
	
	/**
	 * Crea una cola circular de tama�o size y con todos sus campos inicializados a defaultValue.
	 * @param size Tama�o de la cola generada
	 * @param defaultValue Valor por defecto de los campos de la cola.
	 */
	public CircularQueue(int size, float defaultValue) {
		this.size = size;
		queue = new float[size];
		posLastItem = size - 1;
		
		for(int i = 0; i< size;i++){
			queue[i]=-1;
		}
	}
	
	/**
	 * Crea una cola circular de tama�o size y con el �ltimo elemento a�adido en la posici�n
	 * que indica posLastNewElement
	 */
	public CircularQueue(int size, float defaultValue,int posLastElement){
		this.size = size;
		queue = new float[size];
		
		for(int i = 0; i< size;i++){
			queue[i]=-1;
		}
		
		this.posLastItem = posLastElement;
	}

	/**
	 * A�ade el nuevo elemento a la cola eliminando el m�s antiguo
	 * 
	 * @param newItem
	 *            Nuevo elemento a a�adir
	 * @return Posici�n en la cola del elemento a�adido
	 */
	public int addItem(float newItem) {
		posLastItem = (posLastItem + 1) % size;
		queue[posLastItem] = newItem;
		return posLastItem;
	}
	
	/**
	 * A�ade el nuevo elemento durante amount a la cola eliminando el m�s antiguo
	 * @param newItem
	 * @param amount
	 * @return
	 */
	public int addItem(float newItem, int amount){		
		for(int i = 0; i < amount; i++){
			addItem(newItem);
		}
		return posLastItem;
	}
	
	public int addItems(float[] newItems){
		int length = newItems.length;
		for(int i = 0; i < length; i++){
			addItem(newItems[i]);
		}
		return posLastItem;
	}
	
	/**
	 * Adecua la posici�n dada a la posici�n real en la cola.
	 * Corrige los valores negativos y los positivos mayores que el tama�o de
	 * la cola.
	 * @param pos Posici�n en la cola
	 * @return Devuelve la posici�n real en la cola
	 */
	public int correctPos(int pos){
		if (pos < 0) {
			pos += size;
		} else if (pos >= size){
			pos -= size;
		}
		return pos;
	}
	
	/**
	 * Calcula el n�mero de posiciones de diferencia que existe
	 * entre la firstPos y lastPos. 
	 * Nota: admite posiciones negativas.
	 * @param pos1 Posici�n 1
	 * @param pos2 Posici�n 2
	 * @return El n�mero de posiciones de diferencia.
	 */
	public int distanceBetweenPos(int pos1,int pos2){
		pos1 = correctPos(pos1);
		pos2 = correctPos(pos2);
		return (pos2 >= pos1) ? pos2 - pos1 : size
				- (pos1 - pos2);		
	}

	/**
	 * Devuelve el elemento indicado por posici�n. Acepta valores negativos y mayores que el tama�o de la
	 * cola. Si es el caso, calcula la posici�n real en la cola.
	 * @param pos Posici�n del elemento en la cola. Acepta valor negativo y mayores que size. 
	 * @return Devuelve el elemento indicado por posici�n
	 */
	public float getItem(int pos) {		
		return queue[correctPos(pos)];
	}

	/**
	 * @return Devuelve el �ltimo elemento de la cola.
	 */
	public float getLastNewItem(){
		return queue[this.posLastItem];
	}
	/**
	 * Devuelve la posici�n en la cola del �ltimo elemnto a�adido.
	 * 
	 * @return Devuelve la posici�n en la cola del �ltimo elemnto a�adido.
	 */
	public int getPosLastNewItem() {
		return posLastItem;
	}

	/**
	 * @return Devuelve el tama�o de la cola circilar
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param fromItem
	 *            Posici�n del elemento m�s antiguo no utilizado
	 * @param amount
	 *            Cantidad de nuevos elementos que se requieren
	 * @return Devuelve si hay suficientes nuevos elementos en la cola
	 */
	public boolean enoughNewItems(int fromItem, int amount) {		
		int newElements;
		int lastOldElementCorr = correctPos(fromItem);
		newElements = (posLastItem >= lastOldElementCorr) ? posLastItem - lastOldElementCorr: size 
				- (lastOldElementCorr - posLastItem);
		
		if (newElements >= amount) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Copia en un vector no circular los elementos indicados de la cola
	 * 
	 * @param fromPos
	 *            Posici�n(inclusive) a partir de la cual copia los elementos
	 * @param amount
	 *            Cantidad de elementos que se quieren copiar
	 * @return Vector que contiene los elementos copiados.
	 */
	public float[] getArrayA(int fromPos, int amount) {
		float[] result = new float[amount];
		fromPos = correctPos(fromPos);

		int length = Math.min(amount, size-fromPos);
		System.arraycopy(queue, fromPos, result, 0, length);
		
		if(length < amount){
			// Se ha llegado hasta el final de la cola. Se debe copiar los restantes desde el principio.
			System.arraycopy(queue, 0, result, length, amount-length);
		}
		return result;
	}

	/**
	 * Copia en un vector no circular los elementos indicados de la cola
	 * 
	 * @param firstPos
	 *            Posici�n(inclusive) apartir de la cual copia los elementos
	 * @param lastPos
	 *            �ltims posici�n(inclusive) hasta la cual se copian los
	 *            elementos
	 * @return Vector que contiene los elementos copiados.
	 */
	public float[] getArray(int firstPos, int lastPos) {
		int amount;
		firstPos = correctPos(firstPos);
		lastPos = correctPos(lastPos);
		
		amount = (lastPos >= firstPos) ? lastPos - firstPos + 1 : size
				- (firstPos - lastPos) + 1;

		return getArrayA(firstPos, amount);
	}
	
	/**
	 * 
	 * @param pos Posici�n a insertar el nuevo valor. Soporta posiciones negativas.
	 * @param value Valor
	 */
	public void set(int pos,float value){
		queue[correctPos(pos)] = value;
	}
	
	/**
	 * Solo cambia el valor del campo si no coincide este con el valor protegido.
	 * @param pos Posici�n a insertar el nuevo valor. Soporta posiciones negativas.
	 * @param newValue Nuevo valor
	 * @param protectValue Vslor protegido. 
	 */
	public void set(int pos,float newValue,float protectValue){
		pos =  correctPos(pos);
		if(queue[pos] != protectValue){
			queue[correctPos(pos)] = newValue;
		}
	}
	
	/**
	 * Inserta el valor dado en la posici�n relativa indicada por pos.
	 * Se coge como referencia la posici�n del �ltimo elemento.
	 * @param pos Posici�n relativa al �ltimo elemento nuevo. Soporta valores negativos.
	 * @param value Valor nuevo.
	 */
	public void setRelativePos(int pos, float value){
		set(posLastItem+pos, value);
	}
	
	/**
	 * A�ade el valor value a los elementos de la cola indicados.
	 * @param value Nuevo valor de los elementos de la cola
	 * @param pos Posici�n relativa al �ltimo elemento nuevo. Soporta valores negativos.
	 * @param duringPositions N�mero de elementos que cambian.
	 */
	public void setRelativeValue(float value, int pos, int duringPositions){
		setValue(value,posLastItem - pos, posLastItem - pos + duringPositions);
	}

	/**
	 * @param value
	 *            Nuevo valor de los campos
	 * @param fromPosition
	 *            Posici�n (incluida) apartir de la cual se inserta en nuevo
	 *            valor. Puede ser negativa
	 * @param duringPositions
	 *            Numero de posiciones que se cambian.
	 */
	public void setValue(float value, int fromPosition, int duringPositions) {
		// El �ndice puede ser negativo -> tenemos que volverlo positivo
		if (fromPosition < 0) {
			fromPosition += size;
		}
		
		int tmp = fromPosition + duringPositions;
		for (int i = fromPosition; i < tmp; i++) {			
			if (i  < size) {
				queue[i] = value;
			} else {
				queue[i - size] = value;
			}
		}
	}
	
	/**
	 * @param value Suma el valor de value a todos los elementos de la cola
	 */
	public void sum(float value){
		for (int i = 0; i < size; i++){
			queue[i] += value;
		}
	}
	
	/**
	 * Devuelve la suma todos los campos de la cola circular.
	 * @return Devuelve la suma
	 */
	public float sumItems(){
		return sumItems(0,size);
	}
	
	/**
	 * Devuelve la suma todos los campos de la cola circular indicados
	 * @param fromPosition Primera posici�n en la cola que se utiliza
	 * @param duringPositions Cantidad de posiciones que se utilizan. 
	 * Si el valor es negativo, no suma ninguna posici�n.
	 * @return Devuelve la suma
	 */
	public float sumItems(int fromPosition, int duringPositions){
		float result =0;
		fromPosition = this.correctPos(fromPosition);

		for (int i = fromPosition,j=0; j < duringPositions; i++,j++) {
			if (i >= size) {
				i=0;
			}			
			result += queue[i];		
		}

		return result;		
	}
	
	/**
	 * Suma los valores entre ellos de las distintas colas circulares y los coloca en el vector de resultado
	 * @param queues Colas a sumar entre ellas
	 * @param fromPos Desde que posici�n(incluida) debe sumarse
	 * @param duringPositions Durante cuantas posiciones
	 * @param maxValue M�ximo valor que puede llegar la suma. Si se supera, se recorta la suma a maxValue
	 * @return Devuelve un vector donde la posici�n cero de este es la suma de las posiciones de fromPos de 
	 * las colas existentes en queue.
	 */
	public static float[] sum(CircularQueue[] queues,int fromPos, int duringPositions,int maxValue){
		int length = queues.length,j;
		float aux;
		float[] result = new float[duringPositions];
		
		fromPos = queues[0].correctPos(fromPos);		
		for(int i = fromPos,k=0; k < duringPositions;i++,k++){
			aux =0;
			for(j = 0; j < length; j++){
				aux += queues[j].getItem(i);
			}
			result[k] = (aux <= maxValue) ? aux : maxValue;
		}
		return result;
	}
}
