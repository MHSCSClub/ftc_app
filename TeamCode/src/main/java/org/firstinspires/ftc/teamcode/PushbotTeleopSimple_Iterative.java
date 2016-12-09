package org.firstinspires.ftc.teamcode;/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * This file provides basic Telop driving for Group 1's robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common HardwarePushbot hardware class to define the devices on the robot.
 * All device access is managed through the org.firstinspires.ftc.teamcode.HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for Group 1's robot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="HardwarePushbot: Teleop Simple", group="HardwarePushbot")
public class PushbotTeleopSimple_Iterative extends OpMode{

    /* Declare OpMode members. */
    HardwarePushbot robot = new HardwarePushbot();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        updateTelemetry(telemetry);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        float right = gamepad1.left_stick_y - gamepad1.left_stick_x;
        float left = gamepad1.left_stick_y +  gamepad1.left_stick_x;

        robot.frontLeftMotor.setPower(left);
        robot.backLeftMotor.setPower(left);
        robot.frontRightMotor.setPower(right);
        robot.backRightMotor.setPower(right);

        popper();
        pickup();

        // Send telemetry message to signify robot running;
        telemetry.addData("left",  "%.2f", left);
        telemetry.addData("right", "%.2f", right);
        updateTelemetry(telemetry);
    }

    private void popper(){

        if (gamepad2.x) {
            if(!robot.popper.getMode().equals(DcMotor.RunMode.RUN_USING_ENCODER)) {
                robot.popper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            robot.popper.setPower((gamepad2.left_stick_y));
        } else {
            if(robot.popper.getMode().equals(DcMotor.RunMode.RUN_USING_ENCODER)){
                robot.popper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.popper.setTargetPosition(robot.popper.getCurrentPosition());
            }
            if(gamepad2.y){
                robot.popper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.popper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.popper.setTargetPosition(robot.popper.getCurrentPosition());
                robot.popper.setPower(0);
            }
            boolean motorStopped = robot.popper.getTargetPosition() >= robot.popper.getCurrentPosition() - 1
                    && robot.popper.getTargetPosition() <= robot.popper.getTargetPosition() + 1;

            if(gamepad2.right_bumper && motorStopped){
                robot.popper.setTargetPosition(robot.popper.getCurrentPosition() - HardwarePushbot.POPPER_CPR);
                robot.popper.setPower(-1);
            } else if(motorStopped){
                robot.popper.setPower(0);
            }
        }


        telemetry.addData("Popper", "%.2f", (float)robot.popper.getCurrentPosition());
    }

    private void pickup(){
        double power;
        if(gamepad2.a){
            power = 1;
        } else if(gamepad2.b){
            power = -1;
        } else {
            power = 0;
        }
        robot.pickup.setPower(power);
        telemetry.addData("pickup",  "%.2f", power);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        robot.frontLeftMotor.setPower(0);
        robot.frontRightMotor.setPower(0);
        robot.backLeftMotor.setPower(0);
        robot.backRightMotor.setPower(0);
    }

}
