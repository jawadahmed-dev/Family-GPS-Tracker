using Microsoft.EntityFrameworkCore.Migrations;

namespace Family_GPS_Tracker_Api.Migrations
{
    public partial class notifcation_modified : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Notification_Child",
                table: "Notification");

            migrationBuilder.DropForeignKey(
                name: "FK_Notification_Parent",
                table: "Notification");

            migrationBuilder.RenameColumn(
                name: "parent_id",
                table: "Notification",
                newName: "sender_id");

            migrationBuilder.RenameColumn(
                name: "child_id",
                table: "Notification",
                newName: "receiver_id");

            migrationBuilder.RenameIndex(
                name: "IX_Notification_parent_id",
                table: "Notification",
                newName: "IX_Notification_sender_id");

            migrationBuilder.RenameIndex(
                name: "IX_Notification_child_id",
                table: "Notification",
                newName: "IX_Notification_receiver_id");

            migrationBuilder.AddForeignKey(
                name: "FK_Notification_Child",
                table: "Notification",
                column: "sender_id",
                principalTable: "Child",
                principalColumn: "ChildId",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Notification_Parent",
                table: "Notification",
                column: "receiver_id",
                principalTable: "Parent",
                principalColumn: "ParentId",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Notification_Child",
                table: "Notification");

            migrationBuilder.DropForeignKey(
                name: "FK_Notification_Parent",
                table: "Notification");

            migrationBuilder.RenameColumn(
                name: "sender_id",
                table: "Notification",
                newName: "parent_id");

            migrationBuilder.RenameColumn(
                name: "receiver_id",
                table: "Notification",
                newName: "child_id");

            migrationBuilder.RenameIndex(
                name: "IX_Notification_sender_id",
                table: "Notification",
                newName: "IX_Notification_parent_id");

            migrationBuilder.RenameIndex(
                name: "IX_Notification_receiver_id",
                table: "Notification",
                newName: "IX_Notification_child_id");

            migrationBuilder.AddForeignKey(
                name: "FK_Notification_Child",
                table: "Notification",
                column: "child_id",
                principalTable: "Child",
                principalColumn: "ChildId",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Notification_Parent",
                table: "Notification",
                column: "parent_id",
                principalTable: "Parent",
                principalColumn: "ParentId",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
