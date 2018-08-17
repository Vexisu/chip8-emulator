package tk.vexisu.chip8.processor;

import tk.vexisu.chip8.processor.opcode.OpCode;
import tk.vexisu.chip8.processor.opcode.Operator;
import tk.vexisu.chip8.registers.*;

public class Processor
{
	private GeneralPurposeRegisters generalPurposeRegisters = new GeneralPurposeRegisters();
	private IRegister iRegister = new IRegister();
	private ProgramCounterRegister programCounterRegister = new ProgramCounterRegister();
	private StackPointerRegister stackPointerRegister = new StackPointerRegister();
	private StackRegisters stackRegisters = new StackRegisters();
	private TimerRegisters timerRegisters = new TimerRegisters();

	public void tick()
	{
		timerRegisters.tick();
	}

	public void processInstruction(Operator operator)
	{
		switch (OpCode.get(operator))
		{
			case CLS:
				this.cls();
				break;
			case RET:
				this.ret();
				break;
			case JPC:
				this.jpc(operator);
				break;
			case CALL:
				this.call(operator);
				break;
			case SEB:
				this.seb(operator);
				break;
			case SNEB:
				this.sneb(operator);
				break;
			case SEV:
				this.sev(operator);
				break;
			case LDB:
				this.ldb(operator);
				break;
			case ADDB:
				this.addb(operator);
				break;
			case LDV:
				this.ldv(operator);
				break;
		}
	}

	private void cls()
	{
		System.out.println("CLS: Clean screen");
	}

	private void ret()
	{
		var stackPointer = this.stackPointerRegister.read();
		var stackTop = this.stackRegisters.read(stackPointer);
		this.programCounterRegister.write(stackTop);
		this.stackPointerRegister.decrement(1);
	}

	private void jpc(Operator operator)
	{
		var jumpAddress = ((int) operator.getFourBits(2) << 8);
		jumpAddress += ((int) operator.getFourBits(1) << 4);
		jumpAddress += (int) operator.getFourBits(0);
		this.programCounterRegister.write(jumpAddress);
	}

	private void call(Operator operator)
	{
		this.stackPointerRegister.increment(1);
		var stackPointer = this.stackPointerRegister.read();
		var programCounter = this.programCounterRegister.read();
		this.stackRegisters.write(stackPointer, programCounter);
		var callAddress = ((int) operator.getFourBits(2) << 8);
		callAddress += ((int) operator.getFourBits(1) << 4);
		callAddress += (int) operator.getFourBits(0);
		this.programCounterRegister.write(callAddress);
	}

	private void seb(Operator operator)
	{
		var registerAddress = operator.getFourBits(2);
		var comparisonValue = ((int) operator.getFourBits(1) << 4);
		comparisonValue += (int) operator.getFourBits(0);
		var regiterValue = this.generalPurposeRegisters.read(registerAddress);
		if (comparisonValue == regiterValue)
		{
			this.programCounterRegister.increment(2);
		}
	}

	private void sneb(Operator operator)
	{
		var registerAddress = operator.getFourBits(2);
		var comparisonValue = ((int) operator.getFourBits(1) << 4);
		comparisonValue += (int) operator.getFourBits(0);
		var regiterValue = this.generalPurposeRegisters.read(registerAddress);
		if (!(comparisonValue == regiterValue))
		{
			this.programCounterRegister.increment(2);
		}
	}

	private void sev(Operator operator)
	{
		var registerXAddress = operator.getFourBits(2);
		var registerYAddress = operator.getFourBits(1);
		var regiterXValue = this.generalPurposeRegisters.read(registerXAddress);
		var regiterYValue = this.generalPurposeRegisters.read(registerYAddress);
		if (regiterXValue == regiterYValue)
		{
			this.programCounterRegister.increment(2);
		}
	}

	private void ldb(Operator operator)
	{
		var registerAddress = operator.getFourBits(2);
		var valueToStore = (short) (operator.getFourBits(1) << 4);
		valueToStore += operator.getFourBits(0);
		this.generalPurposeRegisters.write(registerAddress, valueToStore);
	}

	private void addb(Operator operator)
	{
		var registerAddress = operator.getFourBits(2);
		var valueToAdd = (short) (operator.getFourBits(1) << 4);
		valueToAdd += operator.getFourBits(0);
		var registerValue = this.generalPurposeRegisters.read(registerAddress);
		registerValue += valueToAdd;
		this.generalPurposeRegisters.write(registerAddress, registerValue);
	}

	private void ldv(Operator operator)
	{
		var registerXAddress = operator.getFourBits(2);
		var registerYAddress = operator.getFourBits(1);
		var registerYValue = this.generalPurposeRegisters.read(registerYAddress);
		this.generalPurposeRegisters.write(registerXAddress, registerYValue);
	}

}
