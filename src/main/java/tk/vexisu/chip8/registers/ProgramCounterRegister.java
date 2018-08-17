package tk.vexisu.chip8.registers;

public class ProgramCounterRegister
{
	private int data = 0x0;

	public int read()
	{
		return data;
	}

	public void write(int data)
	{
		this.data = data;
	}

	public void increment(int value)
	{
		this.data += value;
	}

	public void decrement(int value)
	{
		this.data -= value;
	}
}
